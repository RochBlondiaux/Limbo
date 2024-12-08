package me.rochblondiaux.limbo.server.console;

import java.util.List;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import lombok.RequiredArgsConstructor;
import me.rochblondiaux.limbo.Limbo;

@RequiredArgsConstructor
public class LimboCommandCompleter implements Completer {

    private final Limbo app;

    @Override
    public void complete(LineReader lineReader, ParsedLine parsedLine, List<Candidate> list) {
        System.out.println("Completing command: " + parsedLine.line() + " at position " + parsedLine.cursor() + " with buffer " + parsedLine.line() + " and words " + parsedLine.words() + " and word index " + parsedLine.wordIndex() + " and word cursor " + parsedLine.wordCursor());
    }
}
