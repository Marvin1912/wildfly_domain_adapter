package com.marvin.app.controller;

import com.marvin.camt.model.book_entry.BookingEntryDTO;
import com.marvin.camt.parser.CamtFileParser;
import com.marvin.camt.parser.DocumentUnmarshaller;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

@RestController
public class CamtController {

    private final CamtFileParser camtFileParser;
    private final DocumentUnmarshaller documentUnmarshaller;

    public CamtController(CamtFileParser camtFileParser, DocumentUnmarshaller documentUnmarshaller) {
        this.camtFileParser = camtFileParser;
        this.documentUnmarshaller = documentUnmarshaller;
    }

    @PostMapping(
            path = "/camt-entries",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Flux<BookingEntryDTO> camtEntries(@RequestPart("file") Flux<FilePart> fileMono) {
        return fileMono.flatMap(file -> {

            if (!Objects.requireNonNull(file.filename()).toLowerCase().endsWith(".zip")) {
                return Flux.error(new IllegalArgumentException("Only zip files are allowed"));
            }

            return DataBufferUtils.join(file.content())
                    .flatMapMany(dataBuffer -> {

                        Flux<ByteArrayOutputStream> using = Flua34539a113e0x.using(
                                dataBuffer::asInputStream,
                                camtFileParser::unzipFile,
                                inputStream -> {
                                    try {
                                        inputStream.close();
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                        );

                        DataBufferUtils.release(dataBuffer);

                        return using;
                    })
                    .flatMap(fileContent -> {
                        try {
                            return documentUnmarshaller.unmarshallFile(Flux.just(fileContent));
                        } catch (Exception e) {
                            return Flux.error(new RuntimeException("Error while unmarshalling file", e));
                        }
                    });
        });
    }
}
