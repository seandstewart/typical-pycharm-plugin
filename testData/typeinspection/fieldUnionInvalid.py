import typic
from builtins import *
from typing import Union


@typic.klass
class A:
    a: Union[float, int]


A(<warning descr="Expected type 'Union[float, int]', got 'bytes' instead">a=bytes(123)</warning>)
A(<warning descr="Expected type 'Union[float, int]', got 'str' instead">a=str('123')</warning>)
