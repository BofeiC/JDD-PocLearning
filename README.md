
# JDD-pocLearning
A proof-of-concept tool for generating payloads that exploit unsafe Java object deserialization.

## Description
JDD-pocLearning contains payload constructors for the partial gadget chains detected by JDD (https://github.com/BofeiC/JDD).

- The project provides the corresponding payload constructor code for some of the generic gadget fragments detected by JDD.
- You can utilize them to test the security of your project.

## Disclaimer
This software has been created purely for the purposes of academic research and
for the development of effective defensive techniques, and is not intended to be
used to attack systems except where explicitly authorized. Project maintainers
are not responsible or liable for misuse of the software. Use responsibly.

The use of JDD-PocLearning for illegal attacks or profit is prohibited.

### More data
This is only part of the exploitable gadget chains detected by JDD, if you need more chains, please send us an email with the purpose.

### Citation
If you use JDD (JDD-PocLearning) or some of our code logic, or some of the interesting cases found by JDD, please cite our paper as follows:
```
@inproceedings{chen2024efficient,
  title={Efficient Detection of Java Deserialization Gadget Chains via Bottom-up Gadget Search and Dataflow-aided Payload Construction},
  author={Chen, Bofei and Zhang, Lei and Huang, Xinyou and Cao, Yinzhi and Lian, Keke and Zhang, Yuan and Yang, Min},
  booktitle={2024 IEEE Symposium on Security and Privacy (SP)},
  pages={150--150},
  year={2024},
  organization={IEEE Computer Society}
}
```

### Related open source projects
- JDD: Automated gadget chains detection tool;
  - https://github.com/BofeiC/JDD
- The project is built on `ysoserial` and `marshalsec`, and adds some new exploitable gadget chains not included in these two well-known datasets.
  - https://github.com/frohoff/ysoserial
  - https://github.com/frohoff/marshalsec

## Examples
`git clone https://github.com/BofeiC/JDD-PocLearning.git`
run `GroovyGStr.main`

## Building
Requires Java 8 and Maven 3.x+


## Contributing
1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Requesst
