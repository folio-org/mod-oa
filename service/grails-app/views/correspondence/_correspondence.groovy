import org.olf.oa.Correspondence
import groovy.transform.Field

def should_expand = []

@Field
Correspondence correspondence
json g.render(correspondence, [expand: should_expand])
