import typic
from builtins import *


@typic.klass
class A:
    a: int

    def __init__(self, a):
        self.a = a


A(<warning descr="Expected type 'int', got 'str' instead">a=str('123')</warning>)

