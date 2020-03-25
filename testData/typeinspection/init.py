import typic
from builtins import *


@typic.klass
class A:
    a: int

    def __init__(self, a):
        self.a = a


A(int(123))
