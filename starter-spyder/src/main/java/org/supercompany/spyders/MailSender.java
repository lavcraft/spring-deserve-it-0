package org.supercompany.spyders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class MailSender {

    private final SpiderAlertProperties alertProperties;

    public MailSender(SpiderAlertProperties alertProperties) {
        this.alertProperties = alertProperties;
    }

    public void sendAlert(String ownerName, String spiderType, int spiderHashCode, int remainingLife) {
        System.out.println("Отправлен мейл со следующим содержанием:");
        System.out.printf("Внимание! У паука %s (%s, hashcode: %d) осталась только %d жизнь! Срочно помогите ему, %s!\n",
                ownerName, spiderType, spiderHashCode, remainingLife, alertProperties.getAlertEmail());
    }

    public boolean isAlertEnabled() {
        return alertProperties.getRemainingLife() != null && alertProperties.getAlertEmail() != null;
    }

    public int getAlertThreshold() {
        return alertProperties.getRemainingLife();
    }
}
