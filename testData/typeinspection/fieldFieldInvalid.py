import typic
from builtins import *


@typic.klass
class A:
    a: int = typic.field(int(123))
    b = typic.field(int(123))
    c = typic.field(default=int(123))

A(<warning descr="Expected type 'int', got 'str' instead">a=str('123')</warning>, <warning descr="Expected type 'int', got 'str' instead">b=str('123')</warning>, <warning descr="Expected type 'int', got 'str' instead">c=str('123')</warning>)
