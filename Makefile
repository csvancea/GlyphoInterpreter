JC = javac
JVM = java
JCFLAGS = -cp $(SRC_DIR) -d $(CLS_DIR)
JVMFLAGS = -cp $(CLS_DIR)

ENTRY_POINT = main.Main

SRC_DIR = src
CLS_DIR = build/classes

SRC_FILES = $(shell find $(SRC_DIR)/ -type f -name '*.java')
CLS_FILES = $(patsubst $(SRC_DIR)/%.java, $(CLS_DIR)/%.class, $(SRC_FILES))


.PHONY: build
build: $(CLS_FILES)

.PHONY: run
run: build
	$(JVM) $(JVMFLAGS) $(ENTRY_POINT) $(input) $(base)

.PHONY: clean
clean:
	rm -rf "$(CLS_DIR)"

$(CLS_DIR)/%.class: $(SRC_DIR)/%.java
	@mkdir -p "$(@D)"
	@echo Compiling "$<" ...
	@$(JC) $(JCFLAGS) $<
