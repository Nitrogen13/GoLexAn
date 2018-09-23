# Compilers construction HA-2: Lexical analyzer
*Timur Valiev and Ilgizar Murzakov, BS3-DS-1*

## Description of the project
Program takes Go code from `in.txt`, parses tokens and 
puts parsed tokens and their lexemes to `out.txt`.

This project is written on Kotlin and has following structure: 

````
src/
    main/kotlin/ - source code of the project
        main.kt - entry point of the program
        lexicalAnalysis/
            GoTokeniser.kt - tokenizer of Go code
            stringTools.kt - extension functions for String that helps with parsing
            entities/
                GoSpecification.kt - Regexes and sets of keywords, operators and punctuation for Go
                Tokens.kt - data classes for tokens
    test/kotlin/ - unit tests
````

To run the program open the folder in IDEA as Gradle project, synchronise and 
run `main()` from `src/main/kotlin/main.kt`

### Dependencies
- Kotlin
- Junit 5