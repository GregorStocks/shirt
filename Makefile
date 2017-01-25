all: clean examples

uberjar: src/*/*.clj
	lein uberjar

clean:
	lein clean
	rm -rf examples/*.png examples/*.txt target

SHIRT=java -Djava.awt.headless=true -jar target/uberjar/*standalone.jar

examples: uberjar
	$(SHIRT) -n 2 -s quotes -o png --output-filename examples/quotes-two.png
	$(SHIRT) -n 4 -s quotes -o png --output-filename examples/quotes-four.png
	$(SHIRT) -n 6 -s quotes -o png --output-filename examples/quotes-six.png
	$(SHIRT) -n 8 -s quotes -o png --output-filename examples/quotes-eight.png
	$(SHIRT) -n 10 -s quotes -o png --output-filename examples/quotes-ten.png
