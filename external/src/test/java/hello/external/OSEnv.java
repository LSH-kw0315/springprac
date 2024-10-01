package hello.external;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class OSEnv {

    public static void main(String[] args) {
        Map<String,String> envMap=System.getenv();
        for (String s : envMap.keySet()) {
            log.info("env {} = {}",s,System.getenv(s));
        }
    }
}
