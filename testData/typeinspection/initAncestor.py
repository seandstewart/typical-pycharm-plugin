import typic
from builtins import *


@typic.klass
class A:
    a: int


class B(A):
    def __init__(self, a):
        super().__init__(a=a)


B(a=int(123))
