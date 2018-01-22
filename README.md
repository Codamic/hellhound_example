# HellHound Examples
This repository contains several examples to demonstrate the usage of [HellHound](//hellhound.io).

## Usage

Clone the repository. If you want to use the development version of **HellHound**, clone the
[repo](//github.com/Codamic/hellhound) as well and put it under `checkouts/hellhound` directory
inside the examples clone. Otherwise if you want to just use the HellHound's jar file don't do
anything (before the 1.0.0-rc1 release there might not be a jar file yet).

If you want to run the example against a specific version of HellHound just change the current
branch to the version name. For example for version `1.0.0-rc1` do as follows:

```bash
git checkout 1.0.0-rc1
```

Now it's time to run the examples. In order to run each individual example, grab the namespace name
from the example file and use the namespace in the following command:

```bash
lein run -m <namespace_name>/main
```

That's it.

### Bugs
Please open an issue if you found any bug in the examples or have any question.

## License

Copyright © 2016-2018 Sameer Rahmani <[@lxsameer](//twitter.com/lxsameer)>.

Distributed under the MIT License.
