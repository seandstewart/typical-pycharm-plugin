import typic
from builtins import *


@typic.klass
class A:
    a: int


A(a=int(123))
