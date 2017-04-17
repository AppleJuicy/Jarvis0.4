import jssc.SerialPort;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Created by astrg on 15.02.2017.
 */
public class Jarvis {

    XMLParser xmlParser;
    PostRequest postRequest;
    private SerialPort serialPort;

    public  Jarvis() {
        iniPORT();
        xmlParser = new XMLParser();
        postRequest = new PostRequest();
        JavaSoundRecorder recorder = new JavaSoundRecorder("D:\\speech.wav") {
            @Override
            public void callBack() {
                process();
            }
        };
    }

    void process(){
        try {
            xmlParser.setXml(postRequest.send());
            NodeList items = xmlParser.parse();
            for (int i = 0; i < items.getLength(); i++) {
                Node item = items.item(i);
               // System.out.println("Вариант " + i + ":" + item.getTextContent());

//
//            serialPort.writeByte((byte) 119);
//            serialPort.writeByte((byte) 101);
                if (item.getTextContent().equals("команда 1")) {
                    System.out.println("включить вентеляцию");
                    System.out.println("'q' - желтый");
                    serialPort.writeByte((byte) 113);
                }
                if (item.getTextContent().equals("команда 2")) {
                    System.out.println("Включить теплый пол");
                    System.out.println("'q' - red");
                    serialPort.writeByte((byte) 119);
                }
                if (item.getTextContent().equals("команда 3")) {
                    System.out.println("Включить свет в комнате № 3");
                    System.out.println("'q' - зеленый");
                    serialPort.writeByte((byte) 101);
                }
                if (item.getTextContent().equals("команда 4")) {
                    System.out.println("Выключить весь свет");
                    System.out.println("'f' - off");
                    serialPort.writeByte((byte) 102);
                }
                if (item.getTextContent().equals("команда 5")) System.out.println("Выключить свет");
                if (item.getTextContent().equals("команда 6")) System.out.println("Включить сигнализацию");
                if (item.getTextContent().equals("команда 7")) System.out.println("Выключить сигнализацию");
            }
        }catch (Exception e){
        System.out.printf("ВНИМАНИЕ ПРОБЛЕМЫ СOM-ПОРТОМ!!!");
    }
    }

    void iniPORT(){
        try {
            serialPort = new SerialPort("COM2");
            serialPort.openPort();
            serialPort.setParams(SerialPort.BAUDRATE_38400, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

        }catch (Exception e){
            System.out.printf("ВНИМАНИЕ ПРОБЛЕМЫ СOM-ПОРТОМ!!!");
        }

    }

}
