package hello.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageDecorator implements Component {
    private Component component;

    public MessageDecorator(Component component) {
        this.component = component;
    }


    @Override
    public String operation() {
        log.info("MessageDecorator 실행");
        String result = component.operation();
        String decoratorResult = "*****" + result + "*****";
        log.info("MessageDecorator 적용 전 = {} 후 = {}", result, decoratorResult);
        return decoratorResult;
    }
}