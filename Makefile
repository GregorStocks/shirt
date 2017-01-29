all: clean examples

uberjar: src/*/*.clj
	lein uberjar

clean:
	lein clean
	rm -rf examples/*.png examples/*.txt target

SHIRT=java -Djava.awt.headless=true -jar target/uberjar/*standalone.jar

examples: uberjar
	$(SHIRT) -n 2 -o png --output-filename examples/quotes-two.png
	$(SHIRT) -n 4 -o png --output-filename examples/quotes-four.png
	$(SHIRT) -n 6 -o png --output-filename examples/quotes-six.png
	$(SHIRT) -n 8 -o png --output-filename examples/quotes-eight.png
	$(SHIRT) -n 10 -o png --output-filename examples/quotes-ten.png
	$(SHIRT) -n 2 -b anything -o png --output-filename examples/anything-two.png
	$(SHIRT) -n 4 -b anything -o png --output-filename examples/anything-four.png
	$(SHIRT) -n 6 -b anything -o png --output-filename examples/anything-six.png
	$(SHIRT) -n 8 -b anything -o png --output-filename examples/anything-eight.png
	$(SHIRT) -n 10 -b anything -o png --output-filename examples/anything-ten.png
	
