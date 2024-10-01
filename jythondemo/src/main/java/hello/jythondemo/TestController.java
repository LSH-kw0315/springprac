package hello.jythondemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.python.core.PyFunction;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.Socket;

@RestController
@Slf4j
public class TestController {
    private final int SERVERPORT = 9999;
    private final String IP = "127.0.0.1";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RequestMapping("/test")
    public String Hello() throws IOException {
        log.info("someone is connected!");
        Socket socket = new Socket(IP, SERVERPORT);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter pw = new PrintWriter(socket.getOutputStream());
        try {
            // 서버에 요청 보내기
            pw.flush();

            String json = br.readLine();
            Answer answer = objectMapper.readValue(json, Answer.class);
            log.info("answer is out!");
            return answer.getAnswer();
        } catch (IOException ioException) {
            log.error("error on IO", ioException);
            return null;
        }catch (IllegalArgumentException illegalArgumentException){
            log.error("json is null",illegalArgumentException);
            return null;
        } finally {
            // 소켓 닫기 (연결 끊기)
            try {
                if (socket != null) {
                    socket.close();
                }
                if (br != null) {
                    br.close();
                }
                if (pw != null) {
                    pw.close();
                }
            } catch (IOException e) {
                log.error("error", e);
            }

        }
    }
}
