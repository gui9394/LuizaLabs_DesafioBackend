package com.gui9394.order.process;

import lombok.Getter;

@Getter
public class FileLineProcessException extends Exception {

    private final FileLine fileLine;
    private final FileLineProcessResult result;

    public FileLineProcessException(FileLine fileLine, FileLineProcessResult result) {
        super("Error on process");
        this.fileLine = fileLine;
        this.result = result;
    }

}
