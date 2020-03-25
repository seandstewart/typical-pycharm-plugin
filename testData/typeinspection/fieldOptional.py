import typic
from builtins import *
from typing import Optional


@typic.klass
class A:
    a: Optional[int]


A(a=int(123))
A(a=None)
