package ru.fizteh.fivt.students.gudkov394.telnet;

import org.w3c.dom.Document;
import ru.fizteh.fivt.proxy.LoggingProxyFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.IdentityHashMap;


public class LoggingProxyFactoryClass implements LoggingProxyFactory {

    private ParserXML xmlParser = new ParserXML();

    @Override
    public Object wrap(Writer writer, Object implementation, Class<?> interfaceClass) {
        return Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass},
                new MyHandler(writer, implementation));
    }

    class MyHandler implements InvocationHandler {

        private Writer writer;
        private Object implementation;

        MyHandler(Writer wr, Object impl) {
            writer = wr;
            implementation = impl;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<invoke timestamp=\"")
                    .append(System.currentTimeMillis()).append("\" class=\"")
                    .append(implementation.getClass().getCanonicalName()).append("\" name=\"")
                    .append(method.getName()).append("\">");
            if (args != null && args.length > 0) {
                stringBuilder.append(xmlParser.parse(args));
            } else {
                stringBuilder.append("<arguments/>");
            }
            try {
                Object result;
                if (args != null) {
                    result = method.invoke(implementation, args);
                } else {
                    result = method.invoke(implementation);
                }
                if (method.getReturnType() != void.class) {
                    String res;
                    if (result == null || result instanceof Iterable) {
                        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                        Document document = builder.newDocument();
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                        StringWriter stringWriter = new StringWriter();
                        transformer.transform(new DOMSource(xmlParser.parseObject(result,
                                        new IdentityHashMap<>(), document)),
                                new StreamResult(stringWriter));
                        res = stringWriter.toString();
                    } else {
                        res = result.toString();
                    }
                    stringBuilder.append("<return>").append(res).append("</return>");
                }
                return result;
            } catch (InvocationTargetException e) {
                stringBuilder.append("<thrown>").append(e.getTargetException()).append("</thrown>");
                throw e;
            } finally {
                stringBuilder.append("</invoke>");
                try {
                    writer.write(stringBuilder.toString());
                } catch (IOException e) {
                    System.out.println("Can't write log");
                }
                writer.flush();
            }
        }
    }
}
