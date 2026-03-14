# Report

Submitted report to be manually graded. We encourage you to review the report as you read through the provided
code as it is meant to help you understand some of the concepts. 

## Technical Questions

1. What is the difference between == and .equals in java? Provide a code example of each, where they would return different results for an object. Include the code snippet using the hash marks (```) to create a code block.
> **(1) Answer:** In Java, `==` checks if two variables point to the exact same location in memory
 (reference equality), while `.equals()` checks if two objects have the same internal
values (value equality). For object types, the equality operator performs a referential
equality comparison only, ignoring the object values [1]. This means two separate objects
with identical contents will return `false` with `==` but `true` with `.equals()`.
This distinction matters in this project. In `Filter.java`, when filtering by name,
`.equalsIgnoreCase()` is used instead of `==` so the actual name content is compared
rather than memory addresses. If the `equals()` method is not overridden, the method
from the parent class `Object` is used, and since `Object.equals()` only does a reference
equality check, the behavior might not be what we expect [1]. This is why `BoardGame.java`
overrides both `equals()` and `hashCode()` -- so that the `TreeSet` in `GameList` and
the `HashSet` in `Planner` behave correctly when storing and comparing games.

   ```java
    String a = new String("Go");
    String b = new String("Go");

    System.out.println(a == b);      
    System.out.println(a.equals(b)); 
   ```

2. Logical sorting can be difficult when talking about case. For example, should "apple" come before "Banana" or after? How would you sort a list of strings in a case-insensitive manner?
> **(2) Answer:** In Java, case-sensitive sorting treats uppercase and lowercase letters differently, meaning "Banana" would come before "apple" since uppercase letters have lower ASCII values. To sort case-insensitively, we pass `String.CASE_INSENSITIVE_ORDER` as a comparator to `Collections.sort()` [2]:
#### (2) Pseudocode for example of how to sort list of strings in case-insensitive manner:
```java
List<String> list = Arrays.asList("banana", "Apple", "cherry");
Collections.sort(list, String.CASE_INSENSITIVE_ORDER);
   ```

3. In our version of the solution, we had the following code (snippet)
    ```java
    public static Operations getOperatorFromStr(String str) {
        if (str.contains(">=")) {
            return Operations.GREATER_THAN_EQUALS;
        } else if (str.contains("<=")) {
            return Operations.LESS_THAN_EQUALS;
        } else if (str.contains(">")) {
            return Operations.GREATER_THAN;
        } else if (str.contains("<")) {
            return Operations.LESS_THAN;
        } else if (str.contains("=="))...
    ```
Why would the order in which we checked matter (if it does matter)? Provide examples either way proving your point.

> **(3) Answer:** Yes, the order matters, because some operators are substrings of others [3]. When the method uses `str.contains()`, the first matching condition immediately returns, so a shorter operator could match before the correct longer one.
>
> For example, the operator `">="` contains the character `">"`. If the code checked `">"` first, then a string like `"a >= b"` would incorrectly match the `">"` condition and return `GREATER_THAN` instead of `GREATER_THAN_EQUALS` [3].

**Example of incorrect ordering:**

```java
if (str.contains(">")) {
    return Operations.GREATER_THAN;
} else if (str.contains(">=")) {
    return Operations.GREATER_THAN_EQUALS;
}
```

**Input:**

```
"a >= b"
```

**What happens:**
* `"a >= b"` contains `">"`
* The first condition triggers
* The method incorrectly returns `GREATER_THAN`

> This is why the code correctly checks longer operators first, such as `">="` before `">"` and `"<="` before `"<"` [3].

**Example of correct ordering:**

```java
if (str.contains(">=")) {
    return Operations.GREATER_THAN_EQUALS;
} else if (str.contains(">")) {
    return Operations.GREATER_THAN;
}
```

> Now `"a >= b"` matches `">="` first and returns the correct operation.
Cases where order would not matter occur when the operators are not substrings of each other. For instance, `"=="` does not contain `">"` or `"<"`, so checking `"=="` before or after those would still produce the correct result [3].
Therefore, the order matters because overlapping operators (like `">"` and `">="`) can cause incorrect matches if shorter operators are checked before longer ones. Checking the more specific patterns first prevents this issue [3].


4. What is the difference between a List and a Set in Java? When would you use one over the other? 
> **(4) Answer:** In Java, both `List` and `Set` are part of the Collection framework, but they behave differently in key ways [4]. A `List` is an **ordered, indexed sequence** that allows duplicate elements and multiple null values. Elements can be accessed by their position. Common implementations include `ArrayList`, `LinkedList`, and `Vector` [4]. A `Set` is an **unordered, non-indexed collection** that does **not** allow duplicate elements and can only store one null value. Common implementations include `HashSet` and `LinkedHashSet` [4].
>
> A `List` should be used when order matters and duplicates are allowed, such as storing a sequence of operations or a playlist where the same song can appear twice [4]. A `Set` should be used when uniqueness is required and order does not matter, such as storing a collection of unique usernames or tracking which items have already been visited [4].

**(4) Example:**

```java
List<Integer> l = new ArrayList<>();
l.add(5); l.add(6); l.add(3); l.add(5);
// List = [5, 6, 3, 5]  → duplicates kept, order preserved

Set<Integer> s = new HashSet<>();
s.add(5); s.add(6); s.add(3); s.add(5);
// Set = [3, 5, 6]  → duplicates removed, unordered
```




5. In GamesLoader.java, we use a Map to help figure out the columns. What is a map? Why would we use a Map here? 
> **(5) Answer:** A `Map` in Java is a data structure that stores data as **key-value pairs**, where each key is unique and maps to a specific value [5].
In `GamesLoader.java`, a `Map` is useful for figuring out columns because each column header (key) can be mapped to its index position (value). This makes it easy to look up which column index corresponds to a specific field name like for example, `"title" → 0`, `"genre" → 1` without having to search through the entire array every time.




6. GameData.java is actually an `enum` with special properties we added to help with column name mappings. What is an `enum` in Java? Why would we use it for this application?
> **(6) Answer:** An `enum` in Java is a special data type that defines a fixed set of named constants [6]. Rather than using raw strings or integers to represent a set of known values, an `enum` gives them meaningful names and ensures only valid values can be used.
In `GameData.java`, using an `enum` for column name mappings makes sense because the columns in the data are fixed and known ahead of time. Each constant in the enum can represent a column (e.g., `TITLE`, `GENRE`, `RATING`), and since we added special properties to it, each constant can also store its associated column name string. This prevents typos from raw string comparisons, makes the code more readable, and ensures only valid column names are ever referenced throughout the application [6].




7. Rewrite the following as an if else statement inside the empty code block.
    ```java
    switch (ct) {
                case CMD_QUESTION: // same as help
                case CMD_HELP:
                    processHelp();
                    break;
                case INVALID:
                default:
                    CONSOLE.printf("%s%n", ConsoleText.INVALID);
            }
    ```
**(7) Rewritten code**: 
   ```java
   if (ct == CommandType.CMD_QUESTION || ct == CommandType.CMD_HELP) {
   processHelp();
   } else {
   CONSOLE.printf("%s%n", ConsoleText.INVALID);
   }
   ```

## Deeper Thinking

ConsoleApp.java uses a .properties file that contains all the strings
that are displayed to the client. This is a common pattern in software development
as it can help localize the application for different languages. You can see this
talked about here on [Java Localization – Formatting Messages](https://www.baeldung.com/java-localization-messages-formatting).

Take time to look through the console.properties file, and change some of the messages to
another language (probably the welcome message is easier). It could even be a made up language and for this - and only this - alright to use a translator. See how the main program changes, but there are still limitations in 
the current layout. 

Post a copy of the run with the updated languages below this. Use three back ticks (```) to create a code block. 

```
*******Bienvenue dans le Planificateur BoardGame Arena!*******

Un outil pour aider les gens a planifier
quels jeux ils veulent jouer sur Board Game Arena.

Pour commencer, entrez votre premiere commande, ou tapez ? ou help pour voir les options.
> help

To work with the BGArenaPlanner, you can filter the BGA games list,  
add games to your list, remove games from your list, and save your list to a file.
Filters are progressive, so you can add multiple filters to narrow down the list.

The following commands are available:
exit - exit the program
help or ? [list | filter] - show this help message, Options list - show help for the list command, filter - show help for the filter command.

> filter
Aucun filtre specifie. Affichage du contenu du filtre actuel.
1: 13 Clues
2: 15 Days
...
>
```

Now, thinking about localization - we have the question of why does it matter? The obvious
one is more about market share, but there may be other reasons.  I encourage
you to take time researching localization and the importance of having programs
flexible enough to be localized to different languages and cultures. Maybe pull up data on the
various spoken languages around the world? What about areas with internet access - do they match? Just some ideas to get you started. Another question you are welcome to talk about - what are the dangers of trying to localize your program and doing it wrong? Can you find any examples of that? Business marketing classes love to point out an example of a car name in Mexico that meant something very different in Spanish than it did in English - however [Snopes has shown that is a false tale](https://www.snopes.com/fact-check/chevrolet-nova-name-spanish/).  As a developer, what are some things you can do to reduce 'hick ups' when expanding your program to other languages?


As a reminder, deeper thinking questions are meant to require some research and to be answered in a paragraph for with references. The goal is to open up some of the discussion topics in CS, so you are better informed going into industry.

> Using a `.properties` file to store display strings is a well-established pattern in software development that makes localization far more manageable. In Java, this is supported through the `ResourceBundle` mechanism, whose purpose is to provide an application with localized messages and descriptions that can be externalized to separate files [7]. This means developers can swap out text for different languages without ever touching the core logic of the program. As seen in the BGArenaPlanner, changing the welcome and goodbye messages to French required only editing the `.properties` file — no recompilation or code changes were needed.
>
>However, localization goes far beyond simply translating words. Baeldung notes that distinct cultural or language regions determine not only language-specific descriptions but also currency, number representation, and date and time formats [7]. For example, the number `102,300.45` is written as `102.300,45` in Germany and `102 300,45` in Poland [7]. The current BGArenaPlanner does not account for any of these differences — if the game list included prices or dates, they would still display in the default format regardless of the language set in the properties file.
> here are also deeper pitfalls to watch out for. One common mistake is not accounting for longer words in other languages — when buttons or UI elements are designed with English text in mind, translations in languages like German may simply not fit [8]. Another issue is ignoring dialects: European French and Canadian French use different words and expressions, so localizing for a language rather than a specific region can still feel unnatural to users [8]. The BGArenaPlanner demonstrates this limitation clearly — while the display messages were translated to French, the actual commands (`help`, `filter`, `exit`) still must be typed in English, meaning the application is only partially localized and would still feel foreign to a native French speaker.
>
>In summary, while the `.properties` file approach is a strong first step, true localization requires careful planning around formatting, dialects, UI layout, and even the commands users interact with.

### References 
[1] Baeldung. 2025. Difference Between == and equals() in Java.
Retrieved from https://www.baeldung.com/java-equals-method-operator-difference

[2] S. Sam, "Java Program to sort a List in case insensitive order," *Tutorialspoint*, Mar. 11, 2026. [Online]. Available: https://www.tutorialspoint.com/article/java-program-to-sort-a-list-in-case-insensitive-order. 

[3] Flanagan, D. 2005. Java in a Nutshell. O'Reilly Media, Sebastopol, CA.

[4] GeeksforGeeks, "Difference Between List and Set in Java," *GeeksforGeeks*, Jul. 23, 2025. [Online]. Available: https://www.geeksforgeeks.org/java/difference-between-list-and-set-in-java/. 

[5] Oracle, "Map (Java SE 17 & JDK 17)," *Oracle Java Documentation*, 2021. [Online]. Available: https://docs.oracle.com/en/java/docs/api/java.base/java/util/Map.html. 

[6] Oracle, "Enum Types," *The Java Tutorials*, 2021. [Online]. Available: https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html.

[7] Baeldung, "Java Internationalization – Formatting Messages," *Baeldung*, 2023. [Online]. Available: https://www.baeldung.com/java-8-localization.

[8] Guest Author, "10 Localization Mistakes That Can Cost You Business," *IVANNOVATION*, Jul. 12, 2022. [Online]. Available: https://ivannovation.com/blog/10-localization-mistakes-that-can-cost-you-business/. 
 