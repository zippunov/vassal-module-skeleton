vmod: ## Compile java code and build the VMOD file in the /target
	mvn clean install; \
	file=$$(ls target/*.jar | head -1); \
	fname=$$(basename $$file .jar); \
	cp $$file target/$$fname.vmod

vmod-debug: ## Compile java code with DEBUG meta and build the VMOD file in the /target
	mvn clean install -Pdebug; \
	file=$$(ls target/*.jar | head -1); \
	fname=$$(basename $$file .jar); \
	cp $$file target/$$fname.vmod

run: vmod ## Start new VASSAL game with the VMOD file in the /target
	file=$$(ls target/*.vmod | head -1); \
	java -cp /Applications/VASSAL.app/Contents/Resources/Java/Vengine.jar -Xdock:icon=/Applications/VASSAL.app/Contents/Resources/VASSAL.icns VASSAL.launch.Player --chatlog --load -- $$file

debug: vmod-debug ## Start new VASSAL game in DEBUG mode with the VMOD file in the /target
	file=$$(ls target/*.vmod | head -1); \
	java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 -cp /Applications/VASSAL.app/Contents/Resources/Java/Vengine.jar -Xdock:icon=/Applications/VASSAL.app/Contents/Resources/VASSAL.icns VASSAL.launch.Player --chatlog --load -- $$file

edit: vmod ## Edit with VASSAL the VMOD file in the /target
	file=$$(ls target/*.vmod | head -1); \
	java -cp /Applications/VASSAL.app/Contents/Resources/Java/Vengine.jar -Xdock:icon=/Applications/VASSAL.app/Contents/Resources/VASSAL.icns VASSAL.launch.Editor --edit -- $$file

extract-buildfile: ## Extract buildFile.xml from the VMOD file in the /target and copy-to/overwrite buildFile.xml in the /dist
	file=$$(ls target/*.vmod | head -1); \
	unzip -o -d dist $$file buildFile.xml moduledata;

.PHONY: help

help:
	@grep -E '^[0-9a-zA-Z_-]+:.*?##($$| .*$$)' $(MAKEFILE_LIST) | awk 'BEGIN {FS = ":.*?## ?"}; {printf "\033[1;34m%-30s\033[0m %s\n", $$1, $$2}'

.DEFAULT_GOAL := help