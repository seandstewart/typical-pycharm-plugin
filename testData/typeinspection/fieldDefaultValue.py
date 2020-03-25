import typic
from builtins import *


@typic.klass
class A:
    a = int(123)


A(a=int(123))
