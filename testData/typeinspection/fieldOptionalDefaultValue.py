import typic
from builtins import *
from typing import Optional


@typic.klass
class A:
    a: Optional[int] = None


A(a=int(123))
