package com.example.projectjavafx.validator;

import com.example.projectjavafx.model.Swimmer;

public class SwimmerValidator implements IValidator<Swimmer> {
    @Override
    public void validate(Swimmer entity) throws ValidationException {
        StringBuffer msg=new StringBuffer();
        if (entity.getFirstName()==null)
            msg.append("First name can't be null!");
        if (entity.getLastName()==null)
            msg.append("Last name can't be null!");
        if (msg.length()>0)
            throw new ValidationException(msg.toString());
    }
}
