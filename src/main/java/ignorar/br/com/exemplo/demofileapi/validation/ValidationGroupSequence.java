package ignorar.br.com.exemplo.demofileapi.validation;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({Default.class, EmailConstraint.class})
public interface ValidationGroupSequence {
}
