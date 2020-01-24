package br.com.exemplo.demofileapi.util;

public class Application {

    public static void main(String[] args) {
        FileSplitter fileSplitter= new FileSplitter("inputData"+ File.separator+"inputLargeData.txt",
                "GrossCompaniesData");
        fileSplitter.splitIntoFiles();
    }
}
