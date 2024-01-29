package com.keyin;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class SuggestionEngineTest {
    private SuggestionEngine suggestionEngine = new SuggestionEngine();

    @Mock
    private SuggestionsDatabase mockSuggestionDB;
    private boolean testInstanceSame = false;

    @Test
    public void testGenerateSuggestions() throws Exception {
        suggestionEngine.loadDictionaryData( Paths.get( ClassLoader.getSystemResource("words.txt").getPath()));

//        Assertions.assertTrue(testInstanceSame);
        Assertions.assertTrue(suggestionEngine.generateSuggestions("hellw").contains("hello"));
    }

    @Test
    public void testGenerateSuggestionsFail() throws Exception {
        suggestionEngine.loadDictionaryData( Paths.get( ClassLoader.getSystemResource("words.txt").getPath()));

        testInstanceSame = true;
        Assertions.assertTrue(testInstanceSame);
        Assertions.assertFalse(suggestionEngine.generateSuggestions("hello").contains("hello"));
    }

    @Test
    public void testSuggestionsAsMock() {
        Map<String,Integer> wordMapForTest = new HashMap<>();

        wordMapForTest.put("test", 1);

        Mockito.when(mockSuggestionDB.getWordMap()).thenReturn(wordMapForTest);

        suggestionEngine.setWordSuggestionDB(mockSuggestionDB);

        Assertions.assertFalse(suggestionEngine.generateSuggestions("test").contains("test"));

        Assertions.assertTrue(suggestionEngine.generateSuggestions("tes").contains("test"));
    }

    @Test
    public void testGenerateSuggestionsEmptyWord() throws Exception {
        suggestionEngine.loadDictionaryData(Paths.get(ClassLoader.getSystemResource("words.txt").getPath()));
        String suggestions = suggestionEngine.generateSuggestions("");
        Assertions.assertEquals("", suggestions, "Suggestions should be empty for an empty word");
    }

    @Test
    public void testGenerateSuggestionsUnknownWord() throws Exception {
        suggestionEngine.loadDictionaryData(Paths.get(ClassLoader.getSystemResource("words.txt").getPath()));
        String suggestions = suggestionEngine.generateSuggestions("unknownword");
        Assertions.assertFalse(suggestions.isEmpty(), "Suggestions should not be empty for an unknown word");
    }

    @Test
    public void testGenerateSuggestionsSingleCharacterWord() throws Exception {
        suggestionEngine.loadDictionaryData(Paths.get(ClassLoader.getSystemResource("words.txt").getPath()));
        String suggestions = suggestionEngine.generateSuggestions("a");
        Assertions.assertFalse(suggestions.isEmpty(), "Suggestions should not be empty for a single-character word");
    }
}
