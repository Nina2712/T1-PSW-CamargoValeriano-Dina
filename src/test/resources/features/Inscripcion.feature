Feature: Inscripción a cursos
  Como estudiante de la institución educativa
  Quiero inscribirme en un curso
  Para matricularme en el ciclo académico

  Scenario: Inscripción correcta
    Given un estudiante nuevo que no está inscrito en el curso 2
    When solicita su inscripción en el curso 2
    Then el sistema responde con código 200

  Scenario: Inscripción duplicada
    Given el estudiante "I2024001" ya está inscrito en el curso 1
    When solicita su inscripción en el curso 1
    Then el sistema rechaza la inscripción
