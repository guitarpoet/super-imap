run: $(TARGET)
	$(SILENT) java -jar $(TARGET)
package: $(TARGET)
.PHONY: package

clean:
	$(SILENT) mvn clean
.PHONY: clean

dev: $(ECLIPSE_FILES)
.PHONY: dev

$(TARGET): $(SRC) pom.xml
	$(SILENT) mvn package

$(ECLIPSE_FILES): pom.xml
	$(SILENT) mvn eclipse:eclipse
