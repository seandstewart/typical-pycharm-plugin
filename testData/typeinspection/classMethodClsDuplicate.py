import typic
from builtins import *


@typic.klass
class A:
    a: int

    @classmethod
    def test(cls):
        return cls(int(123))

    @classmethod
    def test(cls):
        return cls(int(123))

    @classmethod
    def test(cls, <error descr="duplicate parameter name">cls</error>):
        return cls(int(123))
