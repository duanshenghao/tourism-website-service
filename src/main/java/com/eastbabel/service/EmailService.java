package com.eastbabel.service;


import com.eastbabel.bo.email.ToEmail;

public interface EmailService {

    void sendEmail(ToEmail toEmail);

}
