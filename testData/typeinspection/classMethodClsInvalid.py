import typic
from builtins import *


@typic.klass
class A:
    a: int

    @classmethod
    def test(cls):
        return cls(<warning descr="Expected type 'int', got 'str' instead">str('123')</warning>)
