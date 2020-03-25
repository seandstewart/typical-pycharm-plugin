import typic
from builtins import *
from typing import Union


@typic.klass
class A:
    a: Union[int, str, None]


A(a=int(123))
A(a=str('123'))
A(a=None)