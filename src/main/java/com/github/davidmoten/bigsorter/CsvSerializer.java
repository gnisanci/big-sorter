package com.github.davidmoten.bigsorter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

final class CsvSerializer implements Serializer<CSVRecord> {

    private final CSVFormat format;
    private final Charset charset;

    CsvSerializer(CSVFormat format, Charset charset) {
        this.format = format;
        this.charset = charset;
    }

    @Override
    public Reader<CSVRecord> createReader(InputStream in) {
        return new Reader<CSVRecord>() {

            Iterator<CSVRecord> it;

            @Override
            public CSVRecord read() throws IOException {
                if (it == null) {
                    it = format.parse(new InputStreamReader(in, charset)).iterator();
                }
                if (it.hasNext()) {
                    CSVRecord v = it.next();
                    System.out.println(v);
                    return v;
                } else {
                    return null;
                }
            }

            @Override
            public void close() throws IOException {
            }
        };
    }

    @Override
    public Writer<CSVRecord> createWriter(OutputStream out) {
        return new Writer<CSVRecord>() {

            CSVPrinter printer;

            @Override
            public void write(CSVRecord value) throws IOException {
                System.out.println("write: "+ value);
                if (printer == null) {
                    Set<String> headers = value.toMap().keySet();
                    printer = format.withHeader(headers.toArray(new String[] {}))
                            .print(new PrintStream(out, false, charset.name()));
                }
                printer.printRecord(value);
            }

            @Override
            public void close() throws IOException {
            }

        };
    }

}
