# Test Feature 

Project testing with functional separation of modules.

Modules 
- HR - human resources
- Planning - planning 
- Finance - finance

## HR

db structure 
- employee
  - id
  - name
  - phone
  - address
  - department
  - position
  - salary
  - start_date
  - end_date
  - status
  - created_at
  - updated_at
  - deleted_at
  - created_by
  - updated_by
  - deleted_by
  - deleted

## Planning

db structure

- project
  - id
  - name
  - description
  - start_date
  - end_date
  - created_at
  - updated_at
  - deleted_at
  - created_by
  - updated_by
  - deleted_by
  - deleted
  
- task
  - id
  - name
  - assigned_to
  - project_id
  - status
  - description
  - start_date
  - end_date
  - created_at
  
- vacation
  - id
  - name
  - employee_id
  - approved_by
  - status
  - description
  - start_date
  - end_date


## Finance

db structure
- payment
  - id
  - name
  - description
  - amount
  - created_at
  - updated_at
  - deleted_at
  - created_by
  - updated_by
  - deleted_by
  - deleted
