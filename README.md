
## Usage

```
$ lein run 4
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

$ lein run 4 heredoc
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

$ lein run 4 quotes
Normal people wearing "Normal people wearing 'Normal people wearing "Normal people wearing 'Normal people scare me' shirts scare me" shirts scare me' shirts scare me" shirts scare me
```

## License

Copyright © 2016 J-Po

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
