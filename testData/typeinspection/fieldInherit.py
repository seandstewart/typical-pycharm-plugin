import typic
from builtins import *


@typic.klass
class A:
    a: int


class B(A):
    pass

B(a=int(123))
