package com.marvin.jms.infrastructure.costs;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

public interface CostDestination extends ApplicationContextAware, InitializingBean {
    void sendMessage(Object dto);
}
