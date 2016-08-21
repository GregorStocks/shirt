examples: src/*/*.clj
	lein run -n 5000 -o text > examples/fivek-heredoc.txt
	lein run -n 5000 -o text -s quotes > examples/fivek-quotes.txt
	lein run -n 2 -o png --output-filename examples/two-heredoc.png
	lein run -n 4 -o png --output-filename examples/four-heredoc.png
	lein run -n 6 -o png --output-filename examples/six-heredoc.png
	lein run -n 8 -o png --output-filename examples/eight-heredoc.png
	lein run -n 10 -o png --output-filename examples/ten-heredoc.png
	lein run -n 2 -s quotes -o png --output-filename examples/two-quotes.png
	lein run -n 4 -s quotes -o png --output-filename examples/four-quotes.png
	lein run -n 6 -s quotes -o png --output-filename examples/six-quotes.png
	lein run -n 8 -s quotes -o png --output-filename examples/eight-quotes.png
	lein run -n 10 -s quotes -o png --output-filename examples/ten-quotes.png
