# encoding: utf-8
# begin bad_consequence.rb

# Clase BadConsequence que representa el mal rollo del monstruo
class BadConsequence
  
  # Consultores / datos miembro
  # -------------------------------------------------------
  
  attr_reader :text                     # Descripción del mal rollo
  attr_reader :levels                   # Nº de niveles perdidos
  attr_reader :nVisibleTreasures        # Nº de tesoros visibles perdidos
  attr_reader :nHiddenTreasures         # Nº de tesoros ocultos perdidos 
  attr_reader :specificVisibleTreasures # Tesoros visibles específicos perdidos
  attr_reader :specificHiddenTreasures  # Tesoros ocultos específicos perdidos
  attr_reader :death                    # Si produce muerte
  
  # -------------------------------------------------------
  
  # Constructores
  # -------------------------------------------------------
  
  # Intialize
  def initialize(aText, someLevels, someVisibleTreasures,
      someHiddenTreasures, someSpecificVisibleTreasures,
      someSpecificHiddenTreasures, death)
    @text = aText
    @levels = someLevels
    @nVisibleTreasures = someVisibleTreasures
    @nHiddenTreasures = someHiddenTreasures
    @specificVisibleTreasures = someSpecificVisibleTreasures
    @specificHiddenTreasures = someSpecificHiddenTreasures
    @death = death
  end
  
  # new es private
  private_class_method :new
  
  # Constructor tipo 1: tesoros genéricos
  def self.newLevelNumberOfTreasures(aText, someLevels, someVisibleTreasures,
      someHiddenTreasures)
    new(aText, someLevels, someVisibleTreasures, someHiddenTreasures, 
      [], [], false)
  end
  
  # Constructor tipo 2: tesoros específicos
  def self.newLevelSpecificTreasures(aText, someLevels, 
      someSpecificVisibleTreasures, someSpecificHiddenTreasures)
    new(aText, someLevels, 0, 0, someSpecificVisibleTreasures, 
      someSpecificHiddenTreasures, false)
  end
  
  # Constructor tipo 3: muerte
  def self.newDeath(aText)
    new(aText, 0, 0, 0, [], [], true)
  end
  
  # -------------------------------------------------------
  
  # Métodos
  # -------------------------------------------------------
  
  # Convierte a string
  def to_s
    message = "\nDescripción: #{@text}\n"
    if !death
      message += "Niveles: #{@levels}\n"
      if @nVisibleTreasures > 0
        message += "Tesoros visibles: #{@nVisibleTreasures}\n"
      elsif !@specificVisibleTreasures.empty?
        message += "Tesoros visibles: #{@specificVisibleTreasures}\n"
      end
      if @nHiddenTreasures > 0
        message += "Tesoros ocultos: #{@nHiddenTreasures}\n"
      elsif !@specificHiddenTreasures.empty?
        message += "Tesoros ocultos: #{@specificHiddenTreasures}\n"
      end
    else
      message += "Muerte: sí"
    end
    message
  end
  
  # -------------------------------------------------------
end

# end bad_consequence-rb