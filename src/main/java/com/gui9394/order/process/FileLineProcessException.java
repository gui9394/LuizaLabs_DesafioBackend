package com.gui9394.order.process;

import lombok.Getter;

@Getter
public class FileLineProcessException extends Exception {

    private final transient FileLine fileLine;
    private final transient FileLineProcessResult result;

    public FileLineProcessException(FileLine fileLine, FileLineProcessResult result) {
        super("Erro ao processar a linha do arquivo");
        this.fileLine = fileLine;
        this.result = result;
    }

}
