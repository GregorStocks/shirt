n degrees of scary normal people wearing shirts. Original by @j-po, graciously packaged, tested, extended, and generally made a real codebase by master software engineer @gregorstocks .

## Usage

```
$ lein run -- --help
  --image-name IMAGENAME  shirt.png  Output filename, including extension
  -o, --output-format FORMAT  text       Output format (text or image)
  -n                                     n for which to render scary(n)
  -s, --string-style                     String style (heredoc or quotes)
  -h, --help
$ lein run -n 4
Normal people wearing <<foofoobar
Normal people wearing <<barbar
Normal people wearing <<foobar
Normal people scare me
foobar
shirts scare me
barbar
shirts scare me
foofoobar
shirts scare me

$ lein run -n 4 -s quotes
Normal people wearing "Normal people wearing 'Normal people wearing "Normal people wearing 'Normal people scare me' shirts scare me" shirts scare me' shirts scare me" shirts scare me
```

## License

Copyright © 2016 J-Po

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
