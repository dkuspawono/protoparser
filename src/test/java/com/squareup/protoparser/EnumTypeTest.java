package com.squareup.protoparser;

import org.junit.Test;

import static com.squareup.protoparser.EnumType.Value;
import static com.squareup.protoparser.TestUtils.NO_OPTIONS;
import static com.squareup.protoparser.TestUtils.NO_VALUES;
import static com.squareup.protoparser.TestUtils.list;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

public class EnumTypeTest {
  @Test public void duplicateTagValuesError() {
    Value value = new Value("Value", 1, "", NO_OPTIONS);
    Type type1 = new EnumType("Enum1", "", "", NO_OPTIONS, list(value));
    Type type2 = new EnumType("Enum2", "", "", NO_OPTIONS, list(value));
    try {
      EnumType.validateValueTagsAreUnique(list(type1, type2));
      fail("Duplicate tag values not allowed.");
    } catch (IllegalStateException e) {
      assertThat(e).hasMessage("Duplicate enum value tag: 1");
    }
  }

  @Test public void emptyToString() {
    EnumType type = new EnumType("Enum", "", "", NO_OPTIONS, NO_VALUES);
    String expected = "enum Enum {}\n";
    assertThat(type.toString()).isEqualTo(expected);
  }

  @Test public void simpleToString() {
    Value one = new Value("ONE", 1, "", NO_OPTIONS);
    Value two = new Value("TWO", 2, "", NO_OPTIONS);
    Value six = new Value("SIX", 6, "", NO_OPTIONS);
    EnumType type = new EnumType("Enum", "", "", NO_OPTIONS, list(one, two, six));
    String expected = ""
        + "enum Enum {\n"
        + "  ONE = 1;\n"
        + "  TWO = 2;\n"
        + "  SIX = 6;\n"
        + "}\n";
    assertThat(type.toString()).isEqualTo(expected);
  }

  @Test public void simpleWithOptionsToString() {
    Value one = new Value("ONE", 1, "", NO_OPTIONS);
    Value two = new Value("TWO", 2, "", NO_OPTIONS);
    Value six = new Value("SIX", 6, "", NO_OPTIONS);
    Option kitKat = new Option("kit", "kat");
    EnumType type = new EnumType("Enum", "", "", list(kitKat), list(one, two, six));
    String expected = ""
        + "enum Enum {\n"
        + "  option kit = \"kat\";\n"
        + "\n"
        + "  ONE = 1;\n"
        + "  TWO = 2;\n"
        + "  SIX = 6;\n"
        + "}\n";
    assertThat(type.toString()).isEqualTo(expected);
  }

  @Test public void simpleWithDocumentationToString() {
    Value one = new Value("ONE", 1, "", NO_OPTIONS);
    Value two = new Value("TWO", 2, "", NO_OPTIONS);
    Value six = new Value("SIX", 6, "", NO_OPTIONS);
    EnumType type = new EnumType("Enum", "", "Hello", NO_OPTIONS, list(one, two, six));
    String expected = ""
        + "// Hello\n"
        + "enum Enum {\n"
        + "  ONE = 1;\n"
        + "  TWO = 2;\n"
        + "  SIX = 6;\n"
        + "}\n";
    assertThat(type.toString()).isEqualTo(expected);
  }

  @Test public void fieldToString() {
    Value value = new Value("NAME", 1, "", NO_OPTIONS);
    String expected = "NAME = 1;\n";
    assertThat(value.toString()).isEqualTo(expected);
  }

  @Test public void fieldWithDocumentationToString() {
    Value value = new Value("NAME", 1, "Hello", NO_OPTIONS);
    String expected = ""
        + "// Hello\n"
        + "NAME = 1;\n";
    assertThat(value.toString()).isEqualTo(expected);
  }

  @Test public void fieldWithOptions() {
    Value value = new Value("NAME", 1, "", list(new Option("kit", "kat")));
    String expected = "NAME = 1 [\n"
        + "  kit = \"kat\"\n"
        + "];\n";
    assertThat(value.toString()).isEqualTo(expected);
  }
}
