def process(params):
    keys = params.split(',')
    for key in keys:
        key_down(key)
        key_up(key)
process(params)