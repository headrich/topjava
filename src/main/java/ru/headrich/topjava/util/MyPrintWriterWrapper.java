package ru.headrich.topjava.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Created by Montana on 08.07.2016.
 */
public class MyPrintWriterWrapper extends PrintWriter {
    public MyPrintWriterWrapper(Writer out) {
        super(out);
    }

    public MyPrintWriterWrapper(OutputStream out) {
        super(out);
    }

    public boolean isClosed(){
            return  out == null;
    }
}
