package com.s360.springjsp.service;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.stereotype.Service;

import com.s360.springjsp.constants.EmailConstants;
import com.s360.springjsp.model.email.send.EmailRequest;

import lombok.extern.slf4j.Slf4j;

import com.sun.mail.imap.IMAPFolder;

@Service
@Slf4j
public class EmailService {

    /**
     *
     */
    public void sendEmail(EmailRequest emailRequest) {

        final String username = "navinetchno@gmail.com";
        final String password = "ebctxfliuwpcmnzu";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); // TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailRequest.getFrom()));

            // Many recepients in to address
            Address[] toAddresses = new InternetAddress[emailRequest.getToAddress().size()];
            for (int i = 0; i < emailRequest.getToAddress().size(); i++) {
                toAddresses[i] = new InternetAddress(emailRequest.getToAddress().get(i));
            }
            message.setRecipients(Message.RecipientType.TO, toAddresses);

            // Many recepients in CC
            Address[] ccAddresses = new InternetAddress[emailRequest.getCc().size()];
            for (int i = 0; i < emailRequest.getCc().size(); i++) {
                ccAddresses[i] = new InternetAddress(emailRequest.getCc().get(i));
            }
            message.setRecipients(Message.RecipientType.CC, ccAddresses);

            message.setSubject(emailRequest.getSubject());
            message.setText(emailRequest.getEmailBody());
            // message.setContent(EmailConstants.EMAIL_HTML_MULTIPART, "text/html");

            BodyPart messageBodyPart1 = new MimeBodyPart();
            messageBodyPart1.setText("This is MimeBodyPart.setText body");
            // Create a multipart message
            Multipart multipart = new MimeMultipart();
            // Part one text message embedded in html
            messageBodyPart1.setContent(EmailConstants.EMAIL_HTML_MULTIPART, "text/html");
            multipart.addBodyPart(messageBodyPart1);

            // Part two is attachment
            BodyPart messageBodyPart2 = new MimeBodyPart();
            String filename = "TestFile.txt";
            DataSource source = new FileDataSource("src\\main\\resources\\" + filename);
            messageBodyPart2.setDataHandler(new DataHandler(source));
            messageBodyPart2.setHeader("Content-ID", "Test1234");
            messageBodyPart2.setFileName(filename);
            multipart.addBodyPart(messageBodyPart2);

            // Part three is image attachment
            BodyPart messageBodyPart3 = new MimeBodyPart();
            String imageName = "Simplify360-color-logo-small.webp";
            DataSource imageSource = new FileDataSource("src\\main\\resources\\static\\" + imageName);
            messageBodyPart3.setDataHandler(new DataHandler(imageSource));
            messageBodyPart3.setHeader("Content-ID", "Test1235");
            messageBodyPart3.setFileName(imageName);
            multipart.addBodyPart(messageBodyPart3);

            message.setContent(multipart);

            Transport.send(message);
            log.info("Email send successfully..!");

        } catch (Exception e) {
            log.error("Error :: msg :{} cause :{}", e.getMessage(), e.getCause());
        }
    }

    public void downloadEmail() {
        String protocol = "imap";
        String host = "imap.gmail.com";
        String port = "993";

        String userName = "navinetchno@gmail.com";
        String password = "yourpassword";
        Properties properties = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties);

        try {
            // connects to the message store
            Store store = session.getStore(protocol);

            store.connect(userName, password);

            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            log.info("Printing all folders: {}", folderInbox.list().toString());

            folderInbox.open(Folder.READ_ONLY);

            // fetches new messages from server
            Message[] messages = folderInbox.getMessages();
            folderInbox.getMessages(5, 10);

            int emailCount = 0;
            for (int i = messages.length - 1; i > 1; i--) {
                Message msg = messages[i];
                Address[] fromAddress = msg.getFrom();
                String from = fromAddress[0].toString();
                String subject = msg.getSubject();
                String toList = parseAddresses(msg.getRecipients(RecipientType.TO));
                String ccList = parseAddresses(msg.getRecipients(RecipientType.CC));
                String sentDate = msg.getSentDate().toString();

                String contentType = msg.getContentType();
                String messageContent = "";

                // store attachment file name, separated by comma
                String attachFiles = "";

                if (contentType.contains("multipart")) {
                    // content may contain attachments
                    Multipart multiPart = (Multipart) msg.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            // this part is attachment
                            String fileName = part.getFileName();
                            attachFiles += fileName + ", ";
                            part.saveFile("src\\test\\resources\\email" + File.separator + fileName);
                        } else {
                            // this part may be the message content
                            messageContent = part.getContent().toString();
                        }
                    }

                    if (attachFiles.length() > 1) {
                        attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                    }
                } else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
                    Object content = msg.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }

                // print out details of each message
                log.info("Message: {} and its uid: {}", (i + 1), msg.getMessageNumber());
                log.info("From: {}", from);
                log.info("To: {}", toList);
                log.info("CC: {}", ccList);
                log.info("Subject: {}", subject);
                log.info("Sent Date: {}", sentDate);
                log.info("Message: {}", messageContent);
                emailCount++;
                if (emailCount == 2) {
                    break;
                }
            }

            // disconnect
            folderInbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void downloadEmailUidFolder() {
        String protocol = "imap";
        String host = "imap.gmail.com";
        String port = "993";

        String userName = "navinetchno@gmail.com";
        String password = "yourpassword";
        Properties properties = getServerProperties(protocol, host, port);
        Session session = Session.getDefaultInstance(properties);

        try {
            // connects to the message store
            Store store = session.getStore(protocol);

            store.connect(userName, password);

            // opens the inbox folder
            IMAPFolder imapFolder = (IMAPFolder)store.getFolder("INBOX");
            Folder [] defaultFolder = store.getDefaultFolder().list("*");
            
            for (Folder folder : defaultFolder) {
                log.info("Printing folder getFullName:{} url: {}",folder.getFullName(),folder.getURLName());
            }
            imapFolder.open(Folder.READ_ONLY);
            
            // fetches new messages from server
            long end = imapFolder.getUIDNext();
            Message[] messages = imapFolder.getMessages((int)end-5, (int)end-1);
            int emailCount = 0;
            for (int i = messages.length - 1; i > 1; i--) {
                Message msg = messages[i];
                Address[] fromAddress = msg.getFrom();
                String from = fromAddress[0].toString();
                String subject = msg.getSubject();
                String toList = parseAddresses(msg.getRecipients(RecipientType.TO));
                String ccList = parseAddresses(msg.getRecipients(RecipientType.CC));
                String sentDate = msg.getSentDate().toString();

                String contentType = msg.getContentType();
                String messageContent = "";

                //store attachment file name, separated by comma
                String attachFiles = "";

                if (contentType.contains("multipart")) {
                    // content may contain attachments
                    Multipart multiPart = (Multipart) msg.getContent();
                    int numberOfParts = multiPart.getCount();
                    for (int partCount = 0; partCount < numberOfParts; partCount++) {
                        MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                        if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                            // this part is attachment
                            String fileName = part.getFileName();
                            attachFiles += fileName + ", ";
                            part.saveFile("src\\test\\resources\\email" + File.separator + fileName);
                        } else {
                            // this part may be the message content
                            messageContent = part.getContent().toString();
                        }
                    }

                    if (attachFiles.length() > 1) {
                        attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
                    }
                } else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
                    Object content = msg.getContent();
                    if (content != null) {
                        messageContent = content.toString();
                    }
                }

                // print out details of each message
                log.info("Message: {} and its uid: {} parent: {}", (i + 1),imapFolder.getUID(msg),imapFolder.getParent());
                log.info("From: {}", from);
                log.info("To: {}", toList);
                log.info("CC: {}", ccList);
                log.info("Subject: {}", subject);
                log.info("Sent Date: {}", sentDate);
                log.info("Message: {}", messageContent);
                emailCount++;
                if (emailCount == 2) {
                    break;
                }
            }

            // disconnect
            imapFolder.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + protocol);
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Returns a list of addresses in String format separated by comma
     *
     * @param address an array of Address objects
     * @return a string represents a list of addresses
     */
    private String parseAddresses(Address[] address) {
        String listAddress = "";

        if (address != null) {
            for (int i = 0; i < address.length; i++) {
                listAddress += address[i].toString() + ", ";
            }
        }
        if (listAddress.length() > 1) {
            listAddress = listAddress.substring(0, listAddress.length() - 2);
        }

        return listAddress;
    }

    private Properties getServerProperties(String protocol, String host, String port) {
        Properties properties = new Properties();

        // server setting
        properties.put(String.format("mail.%s.host", protocol), host);
        properties.put(String.format("mail.%s.port", protocol), port);

        // SSL setting
        properties.setProperty(String.format("mail.%s.socketFactory.class", protocol),
                "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(String.format("mail.%s.socketFactory.fallback", protocol), "false");
        properties.setProperty(String.format("mail.%s.socketFactory.port", protocol), String.valueOf(port));

        return properties;
    }

}
