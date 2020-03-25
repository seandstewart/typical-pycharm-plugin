import typic
from builtins import *
from typing import Optional


@typic.klass
class A:
    a: Optional[int]


A(<warning descr="Expected type 'Optional[int]', got 'str' instead">a=str('123')</warning>)
