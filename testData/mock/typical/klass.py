from types import SimpleNamespace


def klass(cls, *args, **kwargs):
    return cls


def field(*args, **kwargs):
    return Field()


class Field(SimpleNamespace):
    ...
