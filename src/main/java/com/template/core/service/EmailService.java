package com.template.core.service;

import com.template.core.dto.EmailDTO;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Properties;

/**
 * Serviço para manipulação de emails.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class EmailService {

    /**
     * Get sender do properties.
     */
    @Value("${template.email.sender}")
    private String sender;

    /**
     * Get senderPassword do properties.
     */
    @Value("${template.email.senderPassword}")
    private String senderPassword;

    /**
     * Configura Properties para enviar email via gmail.
     *
     * @return o Properties da configuração do gmail
     */
    private Properties getProps() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return props;
    }

    /**
     * Configura Session do email.
     *
     * @return o Session do email
     */
    private Session getSession() {
        return Session.getInstance(getProps(), new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, senderPassword);
            }
        });
    }

    /**
     * Configura Message do email.
     *
     * @return o Message do email
     * @throws MessagingException caso ocorra algum erro na configuração
     */
    private Message getMessage(String email) throws MessagingException {
        Session session = getSession();

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        return message;
    }

    /**
     * Envia um email.
     *
     * @param email Objeto contendo o email do destinatário
     * @throws MessagingException se o email não for enviado com sucesso
     */
    public void sendEmail(EmailDTO email) throws MessagingException {
        Message message = getMessage(email.email());
        message.setSubject("Assunto do email");
        String corpoEmail = "<html>                                                                             " +
                "               <head>                                                                          " +
                "                   <style>                                                                     " +
                "                       body { font-family: Arial, sans-serif; background-color: #f2f2f2; }     " +
                "                       h1 { color: #007bff; }                                                  " +
                "                       p { color: #333; }                                                      " +
                "                   </style>                                                                    " +
                "               </head>                                                                         " +
                "               <body>                                                                          " +
                "                   <h1>Este é um email enviado via JavaMail API!</h1>                          " +
                "                   <p>Olá, <br><br> Este é um exemplo de email com CSS inline.</p>             " +
                "               </body>                                                                         " +
                "           </html>                                                                             ";

        message.setContent(corpoEmail, "text/html");
        Transport.send(message);
    }

}