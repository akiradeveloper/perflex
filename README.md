# perflex [![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/akiradeveloper/perflex?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge)

[() -> Stat] -> Runner -> ReportMaker [Reporter] -> Report

The type signature tells everything you can do with perflex.
All you need to do is define a sequence of tasks each returns meaningful statistics about the run.

## Concept

* There exists [boom](https://github.com/rakyll/boom) but what if testing systems like S3 that requires special authentication?  
* More flexible benchmark framework that executes any work you define in configurable concurrency.  
* Make reports like boom but you can define and append your own new report generator.  

## Output Example

```
Summary:
  Total:   18262 msec
  Slowest: 71756.0
  Fastest: 12162.0
  Average: 15896.725

Response time histogram:
  15141.700195 [       828] | ∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎∎
  21101.101563 [       120] | ∎∎
  27060.500000 [        36] |
  33019.898438 [         9] |
  38979.300781 [         0] |
  44938.699219 [         1] |
  50898.097656 [         2] |
  56857.500000 [         2] |
  62816.898438 [         0] |
  68776.296875 [         1] |

Response time change:
  |                *
  |           *
  |         *
  |           *
  |          *
  |          *
  |          *
  |         *
  |          *
  |         *
  |         *
  |         *
  |         *
  |        *
  |         *
  |        *
  |         *
  |        *
  |        *
  |         *
```

## Developer

Akira Hayakawa (@akiradeveloper)  
e-mail: ruby.wktk@gmail.com)
