This is the output from running the code in Cube.java.

Your task is to derive the values in each matrix based on the associated OpenGL call.

For the points at the end, you only need to show your work for the first one.


After glViewport:
  |      0      0   1200   1200 |

After gluPerspective:
  |   2.41   0.00   0.00   0.00 |
  |   0.00   2.41   0.00   0.00 |
  |   0.00   0.00  -1.00  -0.20 |
  |   0.00   0.00  -1.00   0.00 |

After gluLookAt:
  |   1.00   0.00   0.00   0.00 |
  |   0.00   0.89  -0.45   0.00 |
  |   0.00   0.45   0.89  -4.47 |
  |   0.00   0.00   0.00   1.00 |

After glTranslatef:
  |   1.00   0.00   0.00   0.00 |
  |   0.00   0.89  -0.45   0.45 |
  |   0.00   0.45   0.89  -5.37 |
  |   0.00   0.00   0.00   1.00 |

After glRotatef:
  |   0.50   0.00   0.87   0.00 |
  |   0.39   0.89  -0.22   0.45 |
  |  -0.77   0.45   0.45  -5.37 |
  |   0.00   0.00   0.00   1.00 |

After glScalef:
  |   1.00   0.00   1.73   0.00 |
  |   0.77   1.79  -0.45   0.45 |
  |  -1.55   0.89   0.89  -5.37 |
  |   0.00   0.00   0.00   1.00 |

[   0.50   0.50   0.50 ] -> [  977  185 ]
[   0.50   0.50  -0.50 ] -> [  513  140 ]
[   0.50  -0.50  -0.50 ] -> [  524  567 ]
[   0.50  -0.50   0.50 ] -> [  922  667 ]
[  -0.50  -0.50   0.50 ] -> [  715  934 ]
[  -0.50  -0.50  -0.50 ] -> [  239  762 ]
[  -0.50   0.50  -0.50 ] -> [  169  229 ]
[  -0.50   0.50   0.50 ] -> [  743  314 ]
