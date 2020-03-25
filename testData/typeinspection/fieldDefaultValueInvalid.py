import typic
from builtins import *


@typic.klass
class A:
    a: str = str('abc')


A(<warning descr="Expected type 'str', got 'int' instead">a=int(123)</warning>)
